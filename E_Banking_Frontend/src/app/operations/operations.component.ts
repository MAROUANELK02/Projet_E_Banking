import {Component, OnInit} from '@angular/core';
import {AppStateService} from "../services/app-state.service";
import {OperationsRepositoryService} from "../services/operations.repository.service";

@Component({
  selector: 'app-operations',
  templateUrl: './operations.component.html',
  styleUrl: './operations.component.css'
})
export class OperationsComponent implements OnInit{

  constructor(private operationsRepositoryService: OperationsRepositoryService,
              public appState: AppStateService) {
  }

  searchOperations() {
    this.operationsRepositoryService.searchOperations({});
  }

  ngOnInit(): void {
    this.searchOperations();
  }

  goToPage(number: number) {
    this.appState.setOperationsState({currentPage:number});
    this.searchOperations();
  }

  getDetails(id : number) {

  }

  cancelOperation(id : number) {

  }
}
