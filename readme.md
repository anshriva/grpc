# GRPC
GRPC is one of the best alternatives of REST because
1. GRPC sends data using protobuf encoding and decoding, hence the size of the data sent over wire is very small.  
2. GRPC uses http 2 to communicate, hence it is faster. 

### Disadvantages of REST
1. Http 2 has not yet been used properly yes. There are so many libraries and not all libraries are in sync.  
2. the size of data been sent is larger in REST. 