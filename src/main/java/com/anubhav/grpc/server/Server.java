package com.anubhav.grpc.server;

import com.anubhav.grpc.StudentRequest;
import com.anubhav.grpc.StudentResponse;
import com.anubhav.grpc.StudentServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GRpcService
public class Server extends StudentServiceGrpc.StudentServiceImplBase{

    Logger logger = LoggerFactory.getLogger(Server.class);
    StudentResponse response = StudentResponse.newBuilder()
            .setAge(10).setName("Anubhav")
            .build();
    @Override
    public void getStudent(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        logger.info("got request");
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        logger.info("sent response");
    }

    @Override
    public void getStudentStream(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        logger.info("got request");
        for(int i=0;i<1000000;i++){
            logger.info("going to send response number "+ i);
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
        logger.info("sent response");
    }

    @Override
    public StreamObserver<StudentRequest> sendStudentStream(StreamObserver<StudentResponse> responseObserver) {
        logger.info("got request");
        var responseStream = new StreamObserver<StudentRequest>() {
            @Override
            public void onNext(StudentRequest studentRequest) {
                logger.info("received request to server");
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error("got some error "+ throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                logger.info("completed");
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
        logger.info("sent response");
        return responseStream;
    }


    public StreamObserver<StudentRequest> sendAndGetStudentStream(StreamObserver<StudentResponse> responseObserver) {
        logger.info("going to send and get");
        var res = new StreamObserver<StudentRequest>() {
            @Override
            public void onNext(StudentRequest studentRequest) {
                logger.info("got the request.. sending response");
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error("got error = "+ throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                logger.info("completed");
                responseObserver.onCompleted();
            }
        };

        logger.info("sent response");
        return res;

    }

}