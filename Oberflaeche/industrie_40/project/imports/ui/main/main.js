import angular from 'angular';
import angularMeteor from 'angular-meteor';
import uiRouter from 'angular-ui-router';
import ngMaterial from 'angular-material';
import '../../../node_modules/angular-material/angular-material.css'
import '../own.css';
import template from './main.html';
import {name as Navigation} from '../navigation/navigation';
import {name as Toolbar} from '../toolbar/toolbar';
import {name as View1} from '../view1/view1';
import {name as View3} from '../view3/view3';
// "Haupt"-Javascript-Datei. Hier werden alle Views und Elemente importiert und über die HTML-Datei entsprechend eingeblendet.
// Der Angular-UI-Router erlaubt das dynamische einblenden der Views über die Buttons in der Navigation.html
class Main {
}

const name = 'main';

export default angular.module(name, [
    angularMeteor,
    uiRouter,
    Navigation,
    Toolbar,
    View1,
    View3,
    ngMaterial
]).component(name, {
    template,
    controllerAs: name,
    controller: Main
})
    .config(config);
function config($mdThemingProvider, $urlRouterProvider) {
    'ngInject';
    //festlegen der Standardseite. Wenn ein URL-Pfad unbekannt sein sollte, landet man auf der Seite allgemein
    $urlRouterProvider.otherwise('/Allgemein');
    //festlegen der Farbpalette. Die Farben sind von Angular-Material vorgegeben.
    $mdThemingProvider
        .theme('default')
        .primaryPalette('orange')
        .accentPalette('deep-orange')
        .warnPalette('red');
}