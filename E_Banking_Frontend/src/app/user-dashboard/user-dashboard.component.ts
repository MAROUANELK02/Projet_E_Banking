import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AppStateService} from "../services/app-state.service";
import {CustomersRepositoryService} from "../services/customers.repository.service";
import {OperationsRepositoryService} from "../services/operations.repository.service";

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrl: './user-dashboard.component.css'
})
export class UserDashboardComponent implements OnInit {
  openCloseDetails : boolean = false;
  openCloseTransfer : boolean = false;
  accountForm !: FormGroup;
  type !: string;

  constructor(public appState : AppStateService,
              private customerService : CustomersRepositoryService,
              private operationService : OperationsRepositoryService,
              private fb : FormBuilder) {
  }

  setDetails(open: boolean, type: string = "") {
    this.openCloseDetails = open;
    this.type = type;
    if(this.type.includes("current")) {
      this.searchCurrentAccount();
    }else if(this.type.includes("saving")){
      this.searchSavingAccount();
    }
  }

  setSendMoney(open: boolean, type: string = "") {
    this.openCloseTransfer = open;
    this.type = type;
    if(this.type.includes("current")) {
      this.searchCurrentAccount();
    }else if(this.type.includes("saving")){
      this.searchSavingAccount();
    }
  }

  saveTransaction() {
    this.operationService.transaction(this.accountForm.get("rib")?.value,
      this.accountForm.get("sendTo")?.value, this.accountForm.get("amount")?.value).subscribe(
      ()=> {
        this.setSendMoney(false);
      }
    );
  }

  ngOnInit(): void {
    this.accountForm = this.fb.group({
      rib: this.fb.control(""),
      solde: this.fb.control(""),
      overDraft: this.fb.control(""),
      interestRate: this.fb.control(""),
      status: this.fb.control(""),
      amount: this.fb.control(""),
      sendTo: this.fb.control(""),
      firstName: this.fb.control(""),
      lastName: this.fb.control(""),
    });
  }

  private searchCurrentAccount() {
    this.customerService.getCurrentAccountByCustomerId(this.appState.authState.id).subscribe((accountDetails) => {
      this.appState.currentAccountDetails = accountDetails;
      this.accountForm.patchValue({
        rib: this.appState.currentAccountDetails.id,
        solde: this.appState.currentAccountDetails.balance,
        overDraft: this.appState.currentAccountDetails.overDraft,
        status: this.appState.currentAccountDetails.status,
        firstName: this.appState.currentAccountDetails.customerDTO.firstName,
        lastName: this.appState.currentAccountDetails.customerDTO.lastName
      });
    });
  }

  private searchSavingAccount() {
    this.customerService.getSavingAccountByCustomerId(this.appState.authState.id).subscribe((accountDetails) => {
      this.appState.savingAccountDetails = accountDetails;
      this.accountForm.patchValue({
        rib: this.appState.savingAccountDetails.id,
        solde: this.appState.savingAccountDetails.balance,
        interestRate: this.appState.savingAccountDetails.interestRate,
        status: this.appState.savingAccountDetails.status,
        firstName: this.appState.savingAccountDetails.customerDTO.firstName,
        lastName: this.appState.savingAccountDetails.customerDTO.lastName
      });
    });
  }

}
