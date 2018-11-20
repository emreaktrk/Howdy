package istanbul.codify.monju.api;

import istanbul.codify.monju.logcat.Logcat;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;

final class LogInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private final okhttp3.logging.HttpLoggingInterceptor.Logger logger;
    private volatile okhttp3.logging.HttpLoggingInterceptor.Level level;

    LogInterceptor() {
        this(HttpLoggingInterceptor.Logger.DEFAULT);
    }

    private LogInterceptor(HttpLoggingInterceptor.Logger logger) {
        this.level = okhttp3.logging.HttpLoggingInterceptor.Level.NONE;
        this.logger = logger;
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64L ? buffer.size() : 64L;
            buffer.copyTo(prefix, 0L, byteCount);

            for (int i = 0; i < 16 && !prefix.exhausted(); ++i) {
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }

            return true;
        } catch (EOFException var6) {
            return false;
        }
    }

    public HttpLoggingInterceptor.Level getLevel() {
        return this.level;
    }

    public LogInterceptor setLevel(okhttp3.logging.HttpLoggingInterceptor.Level level) {
        if (level == null) {
            throw new NullPointerException("level == null. Use Level.NONE instead.");
        } else {
            this.level = level;
            return this;
        }
    }

    public Response intercept(Chain chain) throws IOException {
        okhttp3.logging.HttpLoggingInterceptor.Level level = this.level;
        Request request = chain.request();
        if (level == okhttp3.logging.HttpLoggingInterceptor.Level.NONE) {
            return chain.proceed(request);
        } else {
            boolean logBody = level == HttpLoggingInterceptor.Level.BODY;
            boolean logHeaders = logBody || level == okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
            RequestBody requestBody = request.body();
            boolean hasRequestBody = requestBody != null;
            Connection connection = chain.connection();
            Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
            String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;

            Logcat.method(request.method() + " " + request.url());

            if (!logHeaders && hasRequestBody) {
                requestStartMessage = requestStartMessage + " (" + requestBody.contentLength() + "-byte body)";
            }

            this.logger.log(requestStartMessage);
            if (logHeaders) {
                if (hasRequestBody) {
                    if (requestBody.contentType() != null) {
                        this.logger.log("Content-Type: " + requestBody.contentType());
                    }

                    if (requestBody.contentLength() != -1L) {
                        this.logger.log("Content-Length: " + requestBody.contentLength());
                    }
                }

                Headers headers = request.headers();
                int i = 0;

                for (int count = headers.size(); i < count; ++i) {
                    String name = headers.name(i);
                    if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                        this.logger.log(name + ": " + headers.value(i));
                    }
                }

                if (logBody && hasRequestBody) {
                    if (this.bodyEncoded(request.headers())) {
                        this.logger.log("--> END " + request.method() + " (encoded body omitted)");
                    } else {
                        Buffer buffer = new Buffer();
                        requestBody.writeTo(buffer);
                        Charset charset = UTF8;
                        MediaType contentType = requestBody.contentType();
                        if (contentType != null) {
                            charset = contentType.charset(UTF8);
                        }

                        this.logger.log("");
                        if (isPlaintext(buffer)) {
                            Logcat.request(buffer.readString(charset));
                            this.logger.log(buffer.readString(charset));
                            this.logger.log("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
                        } else {
                            this.logger.log("--> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)");
                        }
                    }
                } else {
                    this.logger.log("--> END " + request.method());
                }
            }

            long startNs = System.nanoTime();

            Response response;
            try {
                response = chain.proceed(request);
            } catch (Exception var27) {
                this.logger.log("<-- HTTP FAILED: " + var27);
                throw var27;
            }

            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            String bodySize = contentLength != -1L ? contentLength + "-byte" : "unknown-length";
            this.logger.log("<-- " + response.code() + ' ' + response.message() + ' ' + response
                    .request()
                    .url() + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');
            if (logHeaders) {
                Headers headers = response.headers();
                int i = 0;

                for (int count = headers.size(); i < count; ++i) {
                    this.logger.log(headers.name(i) + ": " + headers.value(i));
                }

                if (logBody && HttpHeaders.hasBody(response)) {
                    if (this.bodyEncoded(response.headers())) {
                        this.logger.log("<-- END HTTP (encoded body omitted)");
                    } else {
                        BufferedSource source = responseBody.source();
                        source.request(9223372036854775807L);
                        Buffer buffer = source.buffer();
                        Charset charset = UTF8;
                        MediaType contentType = responseBody.contentType();
                        if (contentType != null) {
                            try {
                                charset = contentType.charset(UTF8);
                            } catch (UnsupportedCharsetException var26) {
                                this.logger.log("");
                                this.logger.log("Couldn't decode the response body; charset is likely malformed.");
                                this.logger.log("<-- END HTTP");
                                return response;
                            }
                        }

                        if (!isPlaintext(buffer)) {
                            this.logger.log("");
                            this.logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                            return response;
                        }

                        if (contentLength != 0L) {
                            this.logger.log("");
                            this.logger.log(buffer
                                    .clone()
                                    .readString(charset));

                            Logcat.method(request.method() + " " + request.url());
                            Logcat.response(buffer
                                    .clone()
                                    .readString(charset));
                        }

                        this.logger.log("<-- END HTTP (" + buffer.size() + "-byte body)");
                    }
                } else {
                    this.logger.log("<-- END HTTP");
                }
            }

            return response;
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("nationalId");
    }

    public static enum Level {
        NONE,
        BASIC,
        HEADERS,
        BODY;

        private Level() {
        }
    }

    public interface Logger {
        okhttp3.logging.HttpLoggingInterceptor.Logger DEFAULT = message -> Platform
                .get()
                .log(4, message, (Throwable) null);

        void log(String var1);
    }
}