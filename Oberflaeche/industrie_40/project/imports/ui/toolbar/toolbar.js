import angular from 'angular';
import angularMeteor from 'angular-meteor';
import template from './toolbar.html';


const name = 'toolbar';

class Toolbar{
    constructor( $mdSidenav,$mdDialog) {
        'ngInject';
        this.sidenav=$mdSidenav;

        // zeigt die beteiligten Gruppenmitglieder und den Grupennamen an
       this.showGroupMembers=function(){
            $mdDialog.show(
                $mdDialog.alert()
                    .parent(angular.element(document.querySelector('#popupContainer')))
                    .clickOutsideToClose(true)
                    .title('Gruppe Schmitt')
                    .textContent('Teammitglieder: Nico Bollmann, Alexander Deckert, Tim Gaudich, Torsten Schmitt, Malgorzata Wdzieczny')
                    .ariaLabel('Gruppeninfo')
                    .ok('Okay')
                    .targetEvent()
            );
        }
    }
        //toggleLeft und close ermöglichen das Aufklappen und Zuklappen der Navigation.
        toggleLeft(){
           this.sidenav('left').toggle();
        }
        close(){
            this.sidenav('left').close();
        }


}


export default angular.module(name, [
    angularMeteor
]).component(name, {
    template,
    controllerAs: name,
    controller:Toolbar
})


