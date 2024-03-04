# Create DB for MySQL on Azure

Azure Database for MySQL is a relational database service in the Microsoft cloud based on MySQL Community Edition database engine. In this tutorial, we will create sample DB for Java Web Applications.

At first, please configure the following parameter.


```bash
export MYSQL_RES_GRP_NAME=MySQL-RG
export MYSQL_SERVER_NAME=my-mysqlserver
export MYSQL_USER=azureuser
export MYSQL_PASSWORD=
export MYSQL_LOCATION=japaneast
```

|  KEY NAME  |  Description  |
| ---- | ---- |
|  MYSQL_RES_GRP_NAME  |  Resource Group Name of MySQL  |
|  MYSQL_SERVER_NAME  |  Server Name of MySQL  |
|  MYSQL_USER  |  Admin User Name of MySQL  |
|  MYSQL_PASSWORD  |  Admin Password of MySQL  |
|  MYSQL_LOCATION  |  Install Location of MySQL  |

## Create Resource Group for MySQL 


```azurecli
az group create --name $MYSQL_RES_GRP_NAME --location $MYSQL_LOCATION 
```

## Create MySQL Server

```azurecli
az mysql server create \
     --name $MYSQL_SERVER_NAME \
     --resource-group $MYSQL_RES_GRP_NAME \
     --location $MYSQL_LOCATION \
     --admin-user $MYSQL_USER \
     --admin-password $MYSQL_PASSWORD \
     --sku-name GP_Gen5_2
```

## Configure the Firewall Rule to Connect

```azurecli
export PUBLIC_IP=$(curl ifconfig.io)
$ az mysql server firewall-rule create \
    -g MySQL-RG \
    -s my-mysqlserver \
    -n allowip_fromcloudshell \
    --start-ip-address $PUBLIC_IP \
    --end-ip-address $PUBLIC_IP
```

## Connect to MySQL from Client

```bash
$  mysql -u $MYSQL_USER@$MYSQL_SERVER_NAME \
   -h $MYSQL_SERVER_NAME.mysql.database.azure.com -p 
Enter password: 
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 65028
Server version: 5.6.42.0 MySQL Community Server (GPL)

Copyright (c) 2000, 2020, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.
mysql> 
```

## List All of Defult Databases;

```bash
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
4 rows in set (0.09 sec)
```

## Get Sample DB from MySQL Official Site

Get the Sample DB from MySQL.  
[https://dev.mysql.com/doc/index-other.html](https://dev.mysql.com/doc/index-other.html)

World DB.  
[https://downloads.mysql.com/docs/world-db.zip](https://downloads.mysql.com/docs/world-db.zip)

```bash
curl https://downloads.mysql.com/docs/world-db.zip -o world-db.zip
unzip world-db.zip
```

## Create Sample DB and Import Data

```bash
mysql> source world.sql
Query OK, 0 rows affected (0.01 sec)

Query OK, 0 rows affected (0.01 sec)
```

## Evaluate the created DB and TABLES

```bash
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| sys                |
| world              |
+--------------------+
5 rows in set (0.02 sec)

mysql> use world;
Database changed
mysql> show tables;
+-----------------+
| Tables_in_world |
+-----------------+
| city            |
| country         |
| countrylanguage |
+-----------------+
3 rows in set (0.04 sec)
```

```bash
mysql> DESC `city`;
+-------------+----------+------+-----+---------+----------------+
| Field       | Type     | Null | Key | Default | Extra          |
+-------------+----------+------+-----+---------+----------------+
| ID          | int(11)  | NO   | PRI | NULL    | auto_increment |
| Name        | char(35) | NO   |     |         |                |
| CountryCode | char(3)  | NO   | MUL |         |                |
| District    | char(20) | NO   |     |         |                |
| Population  | int(11)  | NO   |     | 0       |                |
+-------------+----------+------+-----+---------+----------------+
5 rows in set (0.01 sec)

mysql> DESC `country`;
+----------------+---------------------------------------------------------------------------------------+------+-----+---------+-------+
| Field          | Type                                                                                  | Null | Key | Default | Extra |
+----------------+---------------------------------------------------------------------------------------+------+-----+---------+-------+
| Code           | char(3)                                                                               | NO   | PRI |         |       |
| Name           | char(52)                                                                              | NO   |     |         |       |
| Continent      | enum('Asia','Europe','North America','Africa','Oceania','Antarctica','South America') | NO   |     | Asia    |       |
| Region         | char(26)                                                                              | NO   |     |         |       |
| SurfaceArea    | decimal(10,2)                                                                         | NO   |     | 0.00    |       |
| IndepYear      | smallint(6)                                                                           | YES  |     | NULL    |       |
| Population     | int(11)                                                                               | NO   |     | 0       |       |
| LifeExpectancy | decimal(3,1)                                                                          | YES  |     | NULL    |       |
| GNP            | decimal(10,2)                                                                         | YES  |     | NULL    |       |
| GNPOld         | decimal(10,2)                                                                         | YES  |     | NULL    |       |
| LocalName      | char(45)                                                                              | NO   |     |         |       |
| GovernmentForm | char(45)                                                                              | NO   |     |         |       |
| HeadOfState    | char(60)                                                                              | YES  |     | NULL    |       |
| Capital        | int(11)                                                                               | YES  |     | NULL    |       |
| Code2          | char(2)                                                                               | NO   |     |         |       |
+----------------+---------------------------------------------------------------------------------------+------+-----+---------+-------+
15 rows in set (0.02 sec)

mysql> DESC `countrylanguage`;
+-------------+---------------+------+-----+---------+-------+
| Field       | Type          | Null | Key | Default | Extra |
+-------------+---------------+------+-----+---------+-------+
| CountryCode | char(3)       | NO   | PRI |         |       |
| Language    | char(30)      | NO   | PRI |         |       |
| IsOfficial  | enum('T','F') | NO   |     | F       |       |
| Percentage  | decimal(4,1)  | NO   |     | 0.0     |       |
+-------------+---------------+------+-----+---------+-------+
4 rows in set (0.02 sec)
```

## Query for Sample Database


### Get Area Informations

```bash
mysql> select distinct Continent from country ;
+---------------+
| Continent     |
+---------------+
| North America |
| Asia          |
| Africa        |
| Europe        |
| South America |
| Oceania       |
| Antarctica    |
+---------------+
```

### Get Country Code in one Continent

```bash
mysql> select code,Name from country where Continent='Asia';
+------+----------------------+
| code | Name                 |
+------+----------------------+
| AFG  | Afghanistan          |
| ARE  | United Arab Emirates |
| ARM  | Armenia              |
| AZE  | Azerbaijan           |
| BGD  | Bangladesh           |
| BHR  | Bahrain              |
| BRN  | Brunei               |
| BTN  | Bhutan               |
| CHN  | China                |
| CYP  | Cyprus               |
| GEO  | Georgia              |
| HKG  | Hong Kong            |
| IDN  | Indonesia            |
| IND  | India                |
| IRN  | Iran                 |
| IRQ  | Iraq                 |
| ISR  | Israel               |
| JOR  | Jordan               |
| JPN  | Japan                |
| KAZ  | Kazakstan            |
| KGZ  | Kyrgyzstan           |
| KHM  | Cambodia             |
| KOR  | South Korea          |
| KWT  | Kuwait               |
| LAO  | Laos                 |
| LBN  | Lebanon              |
| LKA  | Sri Lanka            |
| MAC  | Macao                |
| MDV  | Maldives             |
| MMR  | Myanmar              |
| MNG  | Mongolia             |
| MYS  | Malaysia             |
| NPL  | Nepal                |
| OMN  | Oman                 |
| PAK  | Pakistan             |
| PHL  | Philippines          |
| PRK  | North Korea          |
| PSE  | Palestine            |
| QAT  | Qatar                |
| SAU  | Saudi Arabia         |
| SGP  | Singapore            |
| SYR  | Syria                |
| THA  | Thailand             |
| TJK  | Tajikistan           |
| TKM  | Turkmenistan         |
| TMP  | East Timor           |
| TUR  | Turkey               |
| TWN  | Taiwan               |
| UZB  | Uzbekistan           |
| VNM  | Vietnam              |
| YEM  | Yemen                |
+------+----------------------+
51 rows in set (0.02 sec)
```

### Get All of city

```bash
mysql> select * from city;
+------+-----------------------------------+-------------+----------------------+------------+
| ID   | Name                              | CountryCode | District             | Population |
+------+-----------------------------------+-------------+----------------------+------------+
|    1 | Kabul                             | AFG         | Kabol                |    1780000 |
|    2 | Qandahar                          | AFG         | Qandahar             |     237500 |
...
| 4078 | Nablus                            | PSE         | Nablus               |     100231 |
| 4079 | Rafah                             | PSE         | Rafah                |      92020 |
+------+-----------------------------------+-------------+----------------------+------------+
4079 rows in set (0.05 sec)
```


### Get All city in one Country

```bash
mysql> select * from city where city.CountryCode='JPN' ORDER BY Population DESC;
+------+---------------------+-------------+-----------+------------+
| ID   | Name                | CountryCode | District  | Population |
+------+---------------------+-------------+-----------+------------+
| 1532 | Tokyo               | JPN         | Tokyo-to  |    7980230 |
| 1533 | Jokohama [Yokohama] | JPN         | Kanagawa  |    3339594 |
| 1534 | Osaka               | JPN         | Osaka     |    2595674 |
...
| 1778 | Kashiwazaki         | JPN         | Niigata   |      91229 |
| 1779 | Tsuyama             | JPN         | Okayama   |      91170 |
+------+---------------------+-------------+-----------+------------+
248 rows in set (0.02 sec)
```

### Get the city which the population over 1,000,000

```bash
mysql> select * from city where CountryCode='JPN' AND Population > 1000000 ORDER BY Population DESC;
+------+---------------------+-------------+-----------+------------+
| ID   | Name                | CountryCode | District  | Population |
+------+---------------------+-------------+-----------+------------+
| 1532 | Tokyo               | JPN         | Tokyo-to  |    7980230 |
| 1533 | Jokohama [Yokohama] | JPN         | Kanagawa  |    3339594 |
| 1534 | Osaka               | JPN         | Osaka     |    2595674 |
| 1535 | Nagoya              | JPN         | Aichi     |    2154376 |
| 1536 | Sapporo             | JPN         | Hokkaido  |    1790886 |
| 1537 | Kioto               | JPN         | Kyoto     |    1461974 |
| 1538 | Kobe                | JPN         | Hyogo     |    1425139 |
| 1539 | Fukuoka             | JPN         | Fukuoka   |    1308379 |
| 1540 | Kawasaki            | JPN         | Kanagawa  |    1217359 |
| 1541 | Hiroshima           | JPN         | Hiroshima |    1119117 |
| 1542 | Kitakyushu          | JPN         | Fukuoka   |    1016264 |
+------+---------------------+-------------+-----------+------------+
11 rows in set (0.33 sec)
```

# Configure the VNet access from WebApp to MySQL


After deploy to the Azure Web App,
You need configure the VNet Configuration for MySQL as follows.

## Create VNet

```azurecli
az network vnet create -g MicroProfile \
    --name WebApp-VNET \
    --address-prefixes 10.1.0.0/16 \
    -l japaneast
```

## Create SubNet

```azurecli
az network vnet subnet create \ 
    -g MicroProfile \
    -n Helidon-Subnet \
    --vnet-name WebApp-VNET \
    --address-prefix 10.1.0.0/24 \
    --service-endpoints Microsoft.SQL
```

## Create Virtual Network Integrations from a Azure Webapp

```azurecli
az webapp vnet-integration add -n helidon-hello-azure-1594010320966 \
                               -g MicroProfile \
                               --subnet Helidon-Subnet \
                               --vnet WebApp-VNET
```

## Create Vnet Rule for MySQL

```azurecli
az mysql server vnet-rule create  \
  -n AzureWeAppRule   \
  -g MySQL-RG  \
  -s my-mysqlserver \
  --subnet /subscriptions/f77aafe8-****-****-****-d0c37687ef70/resourceGroups/MicroProfile/providers/Microsoft.Network/virtualNetworks/WebApp-VNET/subnets/Helidon-Subnet
```
