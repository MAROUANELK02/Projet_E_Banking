import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AppStateService} from "../services/app-state.service";
import {ModeratorsRepositoryService} from "../services/moderators.repository.service";
import {Moderator} from "../models/moderator.model";

@Component({
  selector: 'app-moderators',
  templateUrl: './moderators.component.html',
  styleUrl: './moderators.component.css'
})
export class ModeratorsComponent implements OnInit{
  form !: FormGroup;
  openClose : boolean = false;

  constructor(public appState : AppStateService,
              private moderatorService : ModeratorsRepositoryService,
              private fb : FormBuilder) {
  }

  handleAddModerator() {
    this.openClose = true;
  }

  ngOnInit(): void {
    this.searchModerators();
    this.form = this.fb.group({
      id : this.fb.control(""),
      username : this.fb.control(""),
      email : this.fb.control(""),
      password: this.fb.control("")
    });
  }

  goToPage(index: number) {
    this.appState.setModeratorsState({currentPage:index});
    this.searchModerators();
  }

  handleClose() {
    this.openClose = false;
    this.form.reset();
  }

  handleCreate() {
    let moderator : Moderator = new Moderator();
    moderator.id = this.form.value.id;
    moderator.username = this.form.value.username;
    moderator.email = this.form.value.email;
    moderator.password = this.form.value.password;
    this.moderatorService.addModerator(moderator);
    this.openClose = false;
    this.form.reset();
  }

  private searchModerators() {
    this.moderatorService.searchModerators({});
  }
}
