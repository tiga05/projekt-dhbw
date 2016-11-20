import angular from 'angular';
import angularMeteor from 'angular-meteor';
import template from './navigation.html';
import ngProgressBar from 'angular-svg-round-progressbar';
import {Kafkadata} from '../../api/kafkadata';

const name = 'navigation';

class Navigation{
    constructor($mdSidenav,$scope,$reactive) {
        'ngInject';
        this.sidenav = $mdSidenav;
        $reactive(this).attach($scope);
        this.helpers({
            //holt sich die aktuelle OrderNumber abhängig von der höchsten ID in der Datenbank. Höchste ID = aktuellster Auftrag
            getCurrentOrder(){
                var tempvar1=_.pluck(Kafkadata.find({},{limit:1,sort:{_id:-1}},{fields:{orderNumber:1}}).fetch(),'orderNumber');
                this.currentOrderNumber= tempvar1.toString();
                return this.currentOrderNumber;
            },
            //lädt den aktuellen Status des Vorgangs. Dabei wird die Ordernumber die in getCurrentOrder() geholt wurde, benutzt.
            //this.getReactively sorgt dafür, dass die Anfrage erneut ausgeführt wird, falls die Ordernumber sich ändert.
            //Die Anzahl der Einträge zu einer bestimmten Ordernumber verraten den aktuellen Status
            getCurrentStatus(){
                var progressValue= Kafkadata.find({orderNumber:this.getReactively('currentOrderNumber')}).fetch();
                return progressValue.length;
            },

        });
    }
    //toggleLeft und close ermöglichen das Aufklappen und Zuklappen der Navigation.
    toggleLeft(){
        this.sidenav('left').toggle();
    }
    close(){
        this.sidenav('left').close();
    }


}

// create a module
export default angular.module(name, [
    ngProgressBar,
    angularMeteor
]).component(name, {
    template,
    controllerAs: name,
    controller: Navigation
})
