c-match
=======

install postgresql :

    sudo apt-get install postgresql
    sudo -i -u postgres
 
    psql
    create user quentin;
    alter role quentin with CREATEDB;
    CREATE DATABASE cmatch OWNER quentin;
    ALTER USER quentin WITH ENCRYPTED PASSWORD 'quentin';
    \q

revenir à l'user courant, vérifier que tout est ok :
    psql cmatch

install nodejs :

    sudo add-apt-repository ppa:chris-lea/node.js
    sudo apt-get update
    sudo apt-get install nodejs

install yeoman

    sudo npm install -g yo

install ruby for compass

    sudo apt-get install ruby-full rubygems1.8



