package gRPCObjects.interceptors;

import io.grpc.*;
import java.util.HashMap;
import java.util.Objects;

public class ServerInterceptorImpl implements ServerInterceptor {
    HashMap<String, String> restrict = new HashMap<>();

    public void addRestrict(String client, String procedure) {
        restrict.put(client, procedure);
    }
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call,
                                                                 Metadata headers,
                                                                 ServerCallHandler<ReqT, RespT> next) {
        String procedure = call.getMethodDescriptor().getFullMethodName();
        String clientIp = Objects.requireNonNull(call.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR)).
                toString().split(":")[0].replace("/", "");
        if (restrict.containsKey(clientIp) && restrict.get(clientIp).equals(procedure)) {
            return new ServerCall.Listener() {}; // Deliver an empty request
        }
        return next.startCall(call, headers);
    }
}
