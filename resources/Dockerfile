
FROM centos:7
RUN yum -y update && yum-config-manager -y --add-repo https://download.docker.com/linux/centos/docker-ce.repo
RUN yum -y install docker-ce cronie curl yum-utils device-mapper-persistent-data lvm2 java-1.8.0-openjdk && mkdir /root/cron


# ADD crontab.txt /root/cron/crontab
ADD cronrunner.sh /root/cron/cronrunner.sh 
ADD target/cronrunner-1.0-SNAPSHOT-jar-with-dependencies.jar /root/cron/cronrunner-1.0-SNAPSHOT-jar-with-dependencies.jar

ENTRYPOINT cd /root/cron ; java -jar cronrunner-1.0-SNAPSHOT-jar-with-dependencies.jar --initcron && crontab -u root /root/cron/crontab.txt && crond -n
