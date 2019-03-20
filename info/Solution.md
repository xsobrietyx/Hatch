# Difficulties
- Architecture should solve the problem of potentially unlimited amount of devices that
could be connected/attached to the system.
- Architecture should be able to recover after some disaster occurs.
- Solution should be fast.
- Solution should be scalable.
# Solutions
- We should use REST for communication between the devices (simulated in POC) and service platform.
- For proper scalability we can use cloud computing clusters (EC2) and load balancers (ELB) that could be additionally
scaled automatically.
- For provisioning tasks we can use virtual images of application that should be installed to the new virtual machines.
- We should separate the concerns on the infrastructural level as much as possible, application logic should only face
with the business logic, creation of new clusters, recovery etc. should be separated.
- Properly working system also should have a good monetization mechanism/advertising/analytics/continuously evolving new features.
- Additionally solution should be revised continuously in terms of new potentially useful technologies and/or approaches. 
# Risks
- A lot of coupling with the cloud/orchestration services. We completely rely on third party software that
can be cost a lot of money, but it also can solve a huge part of problems that can't be solved with the additional
hardware (mostly) and software involvement from our side. In case of unexpected disaster some important data centers
can be unavailable.
