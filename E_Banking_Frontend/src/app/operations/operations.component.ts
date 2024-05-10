import {Component, OnInit} from '@angular/core';
import {AppStateService} from "../services/app-state.service";
import {OperationsRepositoryService} from "../services/operations.repository.service";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-operations',
  templateUrl: './operations.component.html',
  styleUrl: './operations.component.css'
})
export class OperationsComponent implements OnInit{
  openClose !: boolean;
  form !: FormGroup;

  constructor(private operationsRepositoryService: OperationsRepositoryService,
              public appState: AppStateService,
              private fb : FormBuilder) {
  }

  searchOperations() {
    this.operationsRepositoryService.searchOperations({});
  }

  ngOnInit(): void {
    this.searchOperations();
    this.form = this.fb.group({
      id: this.fb.control(""),
      operationDate: this.fb.control(""),
      amount: this.fb.control(""),
      description: this.fb.control(""),
      bankAccountDTOId: this.fb.control(""),
    });
  }

  goToPage(number: number) {
    this.appState.setOperationsState({currentPage:number});
    this.searchOperations();
  }

  getDetails(id : number) {
    this.openClose = true;
    this.operationsRepositoryService.getOperationDetails(id).subscribe(operation => {
      this.form.patchValue({
        id: operation.id,
        operationDate: operation.operationDate,
        amount: operation.amount,
        description: operation.description,
        bankAccountDTOId: operation.bankAccountDTOId,
      });
      console.log(this.form.value);
    }, error => {
      this.appState.setOperationsState({status:"ERROR", errorMessage:error.statusText});
    });
  }

  closeModal() {
    this.openClose = false;
    this.form.reset();
  }

  cancelOperation(id : number) {
    const confirmed = window.confirm(`Êtes-vous sûr de vouloir annuler cette opération ?`);

    if (!confirmed) {
      return;
    }

    this.operationsRepositoryService.cancelOperation(id).subscribe(() => {
      this.searchOperations();
    });

  }
}
