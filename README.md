# ms-homework 3 - Инфраструктурные паттерны
#### 
- Команды приведены для кластера minikube (windows)
- Команды запускаем из директории kubernetes, где лежат манифесты

#### 1 - Сделаем маппинг arch.homework на IP minikube кластера:
$ `minikube ip`<br/>
hosts: `172.23.222.239 arch.homework`
<br/>

#### 2 - Установим Ingress NGINX через HELM:
$ `cd <project_dir>/kubernetes` <br/>
$ `kubectl create namespace m && helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx/ && helm repo update && helm install nginx ingress-nginx/ingress-nginx --namespace m -f daemon_set.yaml`
<br/>

#### 3 - Установим PostgreSQL через HELM:
$ `helm repo add bitnami https://charts.bitnami.com/bitnami` <br/>
$ `helm install my-release bitnami/postgresql --set global.postgresql.auth.username=postgres --set global.postgresql.auth.password=pass --set global.postgresql.auth.database=postgres` <br/>

#### 4 - Создадим secrets :
$ `kubectl apply -f secret.yaml` <br/>
или создадим их через командную строку  <br/>
$ `kubectl create secret generic ms-hw-4-secrets \
--from-literal=postgres.url=jdbc:postgresql://my-release-postgresql:5432/postgres \
--from-literal=postgres.user=postgres \
--from-literal=postgres.password=pass`  

#### 5 - Запускаем манифесты - Config Map, Roles, Deployment и NGINX:
$ `kubectl apply -f config_map.yaml` <br/>
$ `kubectl apply -f role-bindings.yaml` <br/>
$ `kubectl apply -f deployment.yaml` <br/>
$ `kubectl apply -f nginx-ingress.yaml` <br/>
<br/>

#### 6 - Из корневой директории newman-ом запускаем postman коллекцию ms-hw-4.postman.json:
$ `cd ..` <br/>
$ `newman run ms-hw-4.postman.json` <br/>

