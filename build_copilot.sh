#!/bin/bash
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
