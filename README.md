# FoodScanner

## Description
FoodScanner is an Android App developed by Laurent Di Dionisio and Mohammed Fonseca Boukhalat for the MAS-RAD MWS course.
FoodScanner allows you to scan a product's barcode and retrieve details such as nutritional values, ingredients, and product photos.
## Features
- scan a barcode with the camera
- enter barcode manually
- retrieve product details based on the barcode
- save the product search history
- show the product search history in a list
- offline mode : show product details from the search history list
- delete item from the product search history
## Backlog
You can find the product backlog here [product backlog here](https://trello.com/b/8aljW8R8/foodscanner).
## Tech
### Architecture
FoodScanner tends to implement a software architectural pattern known as Clean Architecture combined with MVVM.
Clean Architecture is one of several MVC implementations, it's based on [SOLID](https://medium.com/backticks-tildes/the-s-o-l-i-d-principles-in-pictures-b34ce2f1e898) principles and has been synthesized by [Robert C. Martin]
MVVM is another MVC flavor.
### Library
FoodScanner use a number of open source projects to work properly:
* Language: Kotlin

* Dependency Injection: Koin

* Design: Material
* Layout: ConstraintLayout, CardView
* Animation Lottie (AirBnB)
* Navigation: Jetpack-Android Navigation

* Camera: Jetpack-CameraX
* Barcode codec: ZXing

* DataBinding: LiveData
* Networking: Retrofit, OKHttp
* Concurrency: Coroutine

* UnitTest: Junit
* UITest: Espresso

## Tools
Some tools that I used for the development of the application:
* [dillinger] - dillinger is an online Markdown editor
* [trello] - trello is a web-based Kanban-style list-making application
## Installation
* Clone the git repository to your local machine
* in your terminal: npm install
* in your terminal: ng serve
* enjoy !
## Miscellaneous
* [Citizen Engagement API documentation]
* username: 'admin', password: 'test' (without quote)
## Todos
License
----
MIT

**Free Software, Hell Yeah!**

   [git-repo-url]: <https://github.com/mofobo/citizen-engagement>
   [node.js]: <http://nodejs.org>
   [AngularJS]: <http://angularjs.org>
   [RxJS]: <https://rxjs-dev.firebaseapp.com>
   [dillinger]:  <https://dillinger.io/>
   [trello]:  <https://trello.com/>
   [Citizen Engagement API documentation]:  <https://mediacomem.github.io/comem-citizen-engagement-api/>

   [Robert C. Martin]: <http://cleancoder.com/products>
