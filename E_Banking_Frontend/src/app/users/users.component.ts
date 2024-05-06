import {Component, OnInit} from '@angular/core';
import {CustomersRepositoryService} from "../services/customers.repository.service";
import {AppStateService} from "../services/app-state.service";
import {User} from "../models/user.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {CurrentAccount} from "../models/current-account.model";
import {CurrentAccountsRepositoryService} from "../services/current-accounts.repository.service";
import {SavingAccountsRepositoryService} from "../services/saving-accounts.repository.service";
import {SavingAccount} from "../models/saving-account.model";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent implements OnInit {
  query : string = "";
  cities : string[] = ["CASABLANCA", "RABAT", "MARRAKECH", "TANGER"];
  customer !: User;
  form !: FormGroup;
  openClose : boolean = false;
  type !: string;
  openCloseAccountType !: boolean;
  formAccount !: FormGroup;
  openCloseAccount !: boolean;

  constructor(private customersService : CustomersRepositoryService,
              private currentAccountsService : CurrentAccountsRepositoryService,
              private savingAccountsService : SavingAccountsRepositoryService,
              public appState : AppStateService,
              private fb : FormBuilder) {
  }

  searchCustomers() {
    this.customersService.searchCustomers({});
  }

  handleAccount(id : number) {
    this.openCloseAccountType = true;
    this.formAccount.patchValue({
      customerId : id,
    });
  }

  ngOnInit(): void {
    this.searchCustomers();
    this.form = this.fb.group({
      firstName : this.fb.control(""),
      lastName : this.fb.control(""),
      city : this.fb.control(""),
      email : this.fb.control(""),
      cin : this.fb.control("")
    })
    this.formAccount = this.fb.group({
      customerId : this.fb.control(""),
      type : this.fb.control(""),
      overDraft : this.fb.control(""),
      interestRate : this.fb.control("")
    })
  };

  handleEdit(id : number) {
    this.type = "edit"
    this.customer = this.appState.getUserByUserId(id);
    this.form.patchValue({
      firstName: this.customer.firstName,
      lastName: this.customer.lastName,
      city: this.customer.ville,
      email: this.customer.email,
      cin: this.customer.cin
    });
    this.openClose = true;
  }

  handleDelete(id : number) {

  }

  goToPage(number: number) {
    this.appState.setUsersState({currentPage:number});
    this.searchCustomers();
  }

  handleSearch() {
    this.customersService.searchCustomers({keyword:this.query, currentPage :0});
  }

  handleAddCustomer() {
    this.type = "add";
    this.openClose = true;
  }

  //ajouter ou modifier un customer
  handleCreate() {
    let user : User = new User();
    user.firstName = this.form.value.firstName;
    user.lastName = this.form.value.lastName;
    user.email = this.form.value.email;
    user.cin = this.form.value.cin;
    user.ville = this.form.value.city;
    if(this.type.includes('add')) {
      this.customersService.createCustomer(user);
    }else{
      this.customersService.updateCustomer(user);
    }
    this.openClose = false;
    this.type = "";
  }

  handleClose() {
    this.openClose = false;
    this.form.patchValue({
      firstName: "",
      lastName: "",
      city: "",
      email: "",
      cin: ""
    });
    this.type = "";
    this.openCloseAccountType = false;
    this.openCloseAccount = false;
  }

  handleTypeAccount() {
    this.openCloseAccountType = false;
    this.openCloseAccount = true;
  }

  saveAccount() {
    if(this.formAccount.value.type.includes('current')) {
      let currentAccount : CurrentAccount = new CurrentAccount();
      currentAccount.overDraft = this.formAccount.value.overDraft;
      this.currentAccountsService.saveAccount(currentAccount, this.formAccount.value.customerId);
    }else{
      let savingAccount : SavingAccount = new SavingAccount();
      savingAccount.interestRate = this.formAccount.value.interestRate;
      this.savingAccountsService.saveAccount(savingAccount, this.formAccount.value.customerId);
    }
    this.openCloseAccount = false;
  }
}
