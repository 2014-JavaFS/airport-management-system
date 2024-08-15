# FROM is building the base image with all the required language development kits for java specifically here
FROM openjdk:17-alpine

# a place to store all of our app data by default, it's common practice
WORKDIR app/

# COPY - periods(.) mean everything from the local directory (to the dockerfile location) will be copied in the workdir
# in the image which is the second period
# Pair this with a .dockerignore always!
COPY . .

# Manual Copy of each individual item
#COPY .mvn/ .mvn
#COPY pom.xml ./
#COPY mvnw ./
RUN chmod +x mvnw

ENV PORT=9999

ENTRYPOINT ["./mvnw", "spring-boot:run"]