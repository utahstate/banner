#!/bin/bash
ZPROD_IP=10.99.2.219
copilot app init banner
copilot init -t "Load Balanced Web Service" -n static --port 80 --deploy no --env recovery
copilot init -t "Load Balanced Web Service" -n banneradmin --port 8080 --deploy no --env recovery
copilot init -t "Load Balanced Web Service" -n applicationnavigator --port 8080 --deploy no --env recovery
copilot init -t "Load Balanced Web Service" -n banneraccessmanagement --port 8080 --deploy no --env recovery
copilot init -t "Load Balanced Web Service" -n extensibility --port 8080 --deploy no --env recovery
copilot init -t "Load Balanced Web Service" -n businessprocessapi --port 8080 --deploy no --env recovery
copilot init -t "Load Balanced Web Service" -n employeeselfservice --port 8080 --deploy no --env recovery
copilot init -t "Load Balanced Web Service" -n facultyselfservice --port 8080 --deploy no --env recovery
copilot init -t "Load Balanced Web Service" -n financeselfservice --port 8080 --deploy no --env recovery
copilot init -t "Load Balanced Web Service" -n generalselfservice --port 8080 --deploy no --env recovery
copilot init -t "Load Balanced Web Service" -n integrationapi --port 8080 --deploy no --env recovery
copilot init -t "Load Balanced Web Service" -n studentapi --port 8080 --deploy no --env recovery
copilot init -t "Load Balanced Web Service" -n studentregistrationssb --port 8080 --deploy no --env recovery
copilot init -t "Load Balanced Web Service" -n studentselfservice --port 8080 --deploy no --env recovery
copilot init -t "Load Balanced Web Service" -n communicationmanagement --port 8080 --deploy no --env recovery
result=$(ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/k8s-config/banner && source .envrc && kubectl get deployment banneradmin -o=jsonpath='{$.spec.template.spec.containers[:1].image}' -n zprod")
yq eval ".image.location |= \"$result\"" -i copilot/banneradmin/manifest.yml
yq eval ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i copilot/banneradmin/manifest.yml
result=$(ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/k8s-config/banner && source .envrc && kubectl get deployment applicationnavigator -o=jsonpath='{$.spec.template.spec.containers[:1].image}' -n zprod")
yq eval ".image.location |= \"$result\"" -i copilot/applicationnavigator/manifest.yml
yq eval ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i copilot/applicationnavigator/manifest.yml
result=$(ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/k8s-config/banner && source .envrc && kubectl get deployment banneraccessmgmt -o=jsonpath='{$.spec.template.spec.containers[:1].image}' -n zprod")
yq eval ".image.location |= \"$result\"" -i copilot/banneraccessmanagement/manifest.yml
yq eval ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i copilot/banneraccessmanagement/manifest.yml
result=$(ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/k8s-config/banner && source .envrc && kubectl get deployment bannerextensibility -o=jsonpath='{$.spec.template.spec.containers[:1].image}' -n zprod")
yq eval ".image.location |= \"$result\"" -i copilot/extensibility/manifest.yml
yq eval ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i copilot/extensibility/manifest.yml
result=$(ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/k8s-config/banner && source .envrc && kubectl get deployment banneradminbpapi -o=jsonpath='{$.spec.template.spec.containers[:1].image}' -n zprod")
yq eval ".image.location |= \"$result\"" -i copilot/businessprocessapi/manifest.yml
yq eval ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i copilot/businessprocessapi/manifest.yml
result=$(ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/k8s-config/banner && source .envrc && kubectl get deployment employeeselfservice -o=jsonpath='{$.spec.template.spec.containers[:1].image}' -n zprod")
yq eval ".image.location |= \"$result\"" -i copilot/employeeselfservice/manifest.yml
yq eval ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i copilot/employeeselfservice/manifest.yml
result=$(ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/k8s-config/banner && source .envrc && kubectl get deployment facultyselfservice -o=jsonpath='{$.spec.template.spec.containers[:1].image}' -n zprod")
yq eval ".image.location |= \"$result\"" -i copilot/facultyselfservice/manifest.yml
yq eval ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i copilot/facultyselfservice/manifest.yml
result=$(ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/k8s-config/banner && source .envrc && kubectl get deployment financeselfservice -o=jsonpath='{$.spec.template.spec.containers[:1].image}' -n zprod")
yq eval ".image.location |= \"$result\"" -i copilot/financeselfservice/manifest.yml
yq eval ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i copilot/financeselfservice/manifest.yml
result=$(ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/k8s-config/banner && source .envrc && kubectl get deployment bannergeneralssb -o=jsonpath='{$.spec.template.spec.containers[:1].image}' -n zprod")
yq eval ".image.location |= \"$result\"" -i copilot/generalselfservice/manifest.yml
yq eval ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i copilot/generalselfservice/manifest.yml
result=$(ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/k8s-config/banner && source .envrc && kubectl get deployment integrationapi -o=jsonpath='{$.spec.template.spec.containers[:1].image}' -n zprod")
yq eval ".image.location |= \"$result\"" -i copilot/integrationapi/manifest.yml
yq eval ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i copilot/integrationapi/manifest.yml
result=$(ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/k8s-config/banner && source .envrc && kubectl get deployment studentapi -o=jsonpath='{$.spec.template.spec.containers[:1].image}' -n zprod")
yq eval ".image.location |= \"$result\"" -i copilot/studentapi/manifest.yml
yq eval ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i copilot/studentapi/manifest.yml
result=$(ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/k8s-config/banner && source .envrc && kubectl get deployment studentregistrationssb -o=jsonpath='{$.spec.template.spec.containers[:1].image}' -n zprod")
yq eval ".image.location |= \"$result\"" -i copilot/studentregistrationssb/manifest.yml
yq eval ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i copilot/studentregistrationssb/manifest.yml
result=$(ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/k8s-config/banner && source .envrc && kubectl get deployment studentselfservice -o=jsonpath='{$.spec.template.spec.containers[:1].image}' -n zprod")
yq eval ".image.location |= \"$result\"" -i copilot/studentselfservice/manifest.yml
yq eval ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i copilot/studentselfservice/manifest.yml
result=$(ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/k8s-config/banner && source .envrc && kubectl get deployment communicationmanagement -o=jsonpath='{$.spec.template.spec.containers[:1].image}' -n zprod")
yq eval ".image.location |= \"$result\"" -i copilot/communicationmanagement/manifest.yml
yq eval ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i copilot/communicationmanagement/manifest.yml
