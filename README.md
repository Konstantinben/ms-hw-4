# ms-homework 4 - Prometheus. Grafana
#### 
- Команды приведены для кластера minikube (windows)
- Команды запускаем из директории kubernetes, где лежат файлы манифестов и values

#### 1 - Сделаем маппинг arch.homework на IP minikube кластера:
$ `minikube ip`<br/>
hosts: `172.23.222.239 arch.homework`
<br/>
#### 2 - Добавим HELM репозитории:
$ `helm repo add prometheus-community https://prometheus-community.github.io/helm-charts` <br/>
$ `helm repo add stable https://charts.helm.sh/stable` <br/>
$ `helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx` <br/>
$ `helm repo update` <br/>

$ `cd <project_dir>/kubernetes` <br/>

#### 2 - Установим kube-prometheus-stack через HELM:
$ `helm install prom prometheus-community/kube-prometheus-stack -f prometheus.yaml --atomic` <br/>

#### 3 - Установим Ingress NGINX через HELM:
$ `kubectl create namespace m` <br/>
$ `helm install nginx ingress-nginx/ingress-nginx --namespace m -f nginx-daemon.yaml --atomic` <br/>

#### 4 - Установим PostgreSQL через HELM:
$ `helm install my-release bitnami/postgresql --set global.postgresql.auth.username=postgres --set global.postgresql.auth.password=pass --set global.postgresql.auth.database=postgres` <br/>

#### 5 - Запускаем манифесты - Config Map, Roles, Deployment и NGINX:
$ `kubectl apply -f secret.yaml` <br/>
$ `kubectl apply -f config_map.yaml` <br/>
$ `kubectl apply -f role-bindings.yaml` <br/>
$ `kubectl apply -f deployment.yaml` <br/>
$ `kubectl apply -f nginx-ingress.yaml` <br/>
$ `kubectl apply -f service_monitor.yaml` <br/>

#### 6 - Из корневой директории newman-ом запускаем postman коллекцию ms-hw-4.postman.json:
$ `cd ..` <br/>
$ `newman run ms-hw-4.postman.json` <br/>

#### 7 - Проверяем установку прометеус:
$ `kubectl port-forward service/prom-kube-prometheus-stack-prometheus 9090:9090` <br/>
В Status -> Service Discovery должен быть: `serviceMonitor/default/app-service/0 (3 / 24 active targets)` <br/>

#### 8 - Проверяем установку графаны:
$ `kubectl port-forward service/prom-grafana  3000:80` <br/>
Логин / пароль: `admin / prom-operator` <br/>

#### 9 - Импортируем дашборд:
$ `./grafana/DASBOARD__RPC_Latency_Errors.json` <br/>

#### 10 - Скриншоты дашбора под нагрузкой и алерты:
$ `./grafana/` <br/>



