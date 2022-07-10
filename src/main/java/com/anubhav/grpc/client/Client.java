package com.anubhav.grpc.client;

import com.anubhav.grpc.StudentRequest;
import com.anubhav.grpc.StudentResponse;
import com.anubhav.grpc.StudentServiceGrpc;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

public class Client {
    static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void getStudents(StudentServiceGrpc.StudentServiceBlockingStub serviceBlockingStub, StudentRequest studentRequest) {
        StopWatch totalTime = new StopWatch();
        totalTime.start();
        serviceBlockingStub.getStudent(studentRequest);
        serviceBlockingStub.getStudent(studentRequest);

        totalTime.stop();
        logger.info("total time = " +  totalTime.getTotalTimeMillis());
    }

    public static void getStudentsStream(StudentServiceGrpc.StudentServiceStub studentServiceStub, StudentRequest studentRequest){
        StopWatch totalTime = new StopWatch();
        totalTime.start();
        logger.info("Going to call to grpc service:  ");
        StreamObserver<StudentResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(StudentResponse studentResponse) {
                logger.info("student response recieved");
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error("error came "+  throwable.getMessage() + " "+ throwable.getStackTrace());
            }

            @Override
            public void onCompleted() {
                logger.info("completed");
            }
        };
        studentServiceStub.getStudentStream(studentRequest,responseObserver);
        totalTime.stop();
        logger.info("total time = " +  totalTime.getTotalTimeMillis());
    }

    public static void sendUserStream(StudentServiceGrpc.StudentServiceStub studentServiceStub, StudentRequest studentRequest){
        StopWatch totalTime = new StopWatch();
        totalTime.start();
        logger.info("Going to call to grpc service:  ");

        StreamObserver<StudentResponse> studentResponseStreamObserver = new StreamObserver<StudentResponse>() {
            @Override
            public void onNext(StudentResponse studentResponse) {
                System.out.println("response got");
                logger.info("received response on client");
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error("got error "+ throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                logger.info("completed");
            }
        };

        StreamObserver<StudentRequest> studentRequestStreamObserver = studentServiceStub.sendStudentStream(studentResponseStreamObserver);

        for(int i=0;i<10000;i++){
            logger.info("going to send student number "+ i);
            studentRequestStreamObserver.onNext(studentRequest);
        }
        totalTime.stop();
        logger.info("total time = " +  totalTime.getTotalTimeMillis());
        studentRequestStreamObserver.onCompleted();
    }


    public static void GetAndSendStudent(StudentServiceGrpc.StudentServiceStub studentServiceStub, StudentRequest studentRequest){
        StopWatch totalTime = new StopWatch();
        totalTime.start();
        StreamObserver<StudentResponse> studentResponseStreamObserver = new StreamObserver<StudentResponse>() {
            @Override
            public void onNext(StudentResponse studentResponse) {
                System.out.println("response got");
                logger.info("received response on client");
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error("got error "+ throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                logger.info("completed");
            }
        };
        logger.info("Going to call to grpc service:  ");
        StreamObserver<StudentRequest> studentRequestStreamObserver = studentServiceStub.sendAndGetStudentStream(studentResponseStreamObserver);

        for(int i=0;i<10000;i++){
            logger.info("going to send student number "+ i);
            studentRequestStreamObserver.onNext(studentRequest);
        }
        logger.info("stop");
        studentRequestStreamObserver.onCompleted();
        logger.info("total time = " +  totalTime.getTotalTimeMillis());
    }
}

