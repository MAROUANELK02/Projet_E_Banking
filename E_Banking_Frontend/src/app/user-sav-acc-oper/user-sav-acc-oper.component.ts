import {Component, OnInit} from '@angular/core';
import {AppStateService} from "../services/app-state.service";
import {OperationsRepositoryService} from "../services/operations.repository.service";

@Component({
  selector: 'app-user-sav-acc-oper',
  templateUrl: './user-sav-acc-oper.component.html',
  styleUrl: './user-sav-acc-oper.component.css'
})
export class UserSavAccOperComponent implements OnInit{
  constructor(public appState : AppStateService,
              private operationService: OperationsRepositoryService) {
  }

  goToPage(index: number) {
    this.appState.operationsState.currentPage = index;
    this.searchOperationsHistory();
  }

  searchOperationsHistory() {
    this.appState.operationsState.status = "LOADING";
    this.operationService.searchSavAccId(this.appState.authState.id).subscribe({
      next: (currentAccount) => {
        this.operationService.searchSavAccOperationsHistory(currentAccount.id).subscribe({
          next: (response ) => {
            let operations = response.content;
            let totalPages = response.totalPages;
            this.appState.setOperationsState({operations, totalPages, status: "LOADED", errorMessage: ""});
          },
          error: (err) => {
            this.appState.setOperationsState({status: "ERROR", errorMessage: err.statusText});
          }
        });
      },
      error: (err) => {
        this.appState.setOperationsState({status: "ERROR", errorMessage: err.statusText});
      }
    });
  }

  ngOnInit(): void {
    this.searchOperationsHistory();
  }

}
