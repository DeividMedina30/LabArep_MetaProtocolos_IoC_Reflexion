# AREP- ARQUITECTURAS EMPRESARIAL.

## TALLER DE ARQUITECTURAS DE SERVIDORES DE APLICACIONES, META PROTOCOLOS DE OBJETOS, PATRÓN IOC, REFLEXIÓN

### INTRODUCCIÓN.

Para este taller los estudiantes deberán construir un servidor Web (tipo Apache) en Java. El servidor debe ser capaz
de entregar páginas html e imágenes tipo PNG. Igualmente el servidor debe proveer un framework IoC
para la construcción de aplicaciones web a partir de POJOS. Usando el servidor se debe construir
una aplicación Web de ejemplo y desplegarlo en Heroku. El servidor debe atender múltiples solicitudes
no concurrentes.
Para este taller desarrolle un prototipo mínimo que demuestre capcidades reflexivas de JAVA y
permita por lo menos cargar un bean (POJO) y derivar una aplicación Web a partir de él. Debe
entregar su trabajo al final del laboratorio.

### PASOS PARA CLONAR.

-  Nos dirigimos a la parte superior de nuestra ubicación, donde daremos click y escribimos la palabra cmd, luego damos enter, con el fin de desplegar
   el Command Prompt, el cual es necesario.

![img1.png](https://i.postimg.cc/GmSNVZZL/img1.png)

![Imagen2.png](https://i.postimg.cc/vB5N1DDT/Imagen2.png)

![Imagen3.png](https://i.postimg.cc/T3hNVthZ/Imagen3.png)

- Una vez desplegado el Command Prompt, pasamos a verificar que tengamos instalado git, ya que sin él no podremos realizar la descarga.
  Para esto ejecutamos el siguiente comando.

`git --version`

![Imagen4.png](https://i.postimg.cc/nh5R0qDM/Imagen4.png)

- Si contamos con git instalado, tendra que salir algo similar. La version puede variar de cuando se este realizando este tutorial.
  Si no cuenta con git, puede ver este tutorial.

[Instalación de Git][id/name]

[id/name]: https://www.youtube.com/watch?v=cYLapo1FFmA

![Imagen5.png](https://i.postimg.cc/fR6CxZG9/Imagen5.png)

-  Una vez comprobado de que contamos con git. pasamos a escribir el siguiente comando. git clone,
   que significa que clonamos el repositorio, y damos la url del repositorio.

`$ git clone https://github.com/DeividMedina30/LabArep_MetaProtocolos_IoC_Reflexion.git`

![Imagen6.png](https://i.postimg.cc/gjkHY0Zf/Imagen6.png)

- Luego podemos acceder al proyecto escribiendo.

`$ cd Lab4_Arep_DeividMedina`

### DESPLEGANDO API EN HEROKU.

[![ProjectDesign](https://www.herokucdn.com/deploy/button.png)](https://labcuatroarep.herokuapp.com/)

#### Respuesta a los siguientes Links.

- `<Obteniendo Pagina html>` : <https://labcuatroarep.herokuapp.com/index.html>

- `<Obteniendo Imagen>` : <https://labcuatroarep.herokuapp.com/gato.png>

#### Respuesta a las siguientes apps.

- `<App que suma dos números>` : <https://labcuatroarep.herokuapp.com/app/calculadoraSumar>

- `<App que resta dos números>` : <https://labcuatroarep.herokuapp.com/app/calculadoraRestar>

### TECNOLOGÍAS USADAS PARA EL DESARROLLO DEL LABORATORIO.

* [Maven](https://maven.apache.org/) - Administrador de dependencias.

* [Heroku](https://heroku.com) - Plataforma de despliegue en la nube.

### LIMITACIONES.

Para este laboratorio las limitaciones que se lograron encontrar fue en la implementación de apps, ya que
se esperaba por lo menos, poder crear una app que sume dos números y una que reste dos números.
Además de no lograr completamente la ejecución en parelelo de multiples solicitudes.

### EXTENDER.

Se espera que pueda llegar a extenderse con los puntos mencionados en las limitaciones, además de crear nuevas
funcionalidades los cuales mejores nuestro servidor. Como lo son multiples solicitudes de
clientes en paralelo.

### Documentación

Para generar la documentación se debe ejecutar:

`$ mvn javadoc:javadoc`

Esta quedará en la carpeta target/site/apidocs

### AUTOR.

> Deivid Sebastián Medina Rativa.

### Licencia.

Este laboratorio esta bajo la licencia de GNU GENERAL PUBLIC LICENSE.
