package gRPCObjects.interceptors;

import io.grpc.*;
import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

public class ClientInterceptorImpl implements ClientInterceptor {
    int channel;
    public ClientInterceptorImpl(int channel) {
        this.channel = channel;
    }
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.
                SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put( Metadata.Key.of("channel", ASCII_STRING_MARSHALLER), String.valueOf(channel));
                super.start(responseListener, headers);
            }
        };
    }
}
