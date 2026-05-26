import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-customer-edit',
  imports: [],
  templateUrl: './customer-edit.html',
  styleUrl: './customer-edit.scss',
})
export class CustomerEdit {

  id!: number;

  constructor(private route: ActivatedRoute){}

  ngOnInit(){
    this.id = Number(this.route.snapshot.paramMap.get('id'));
  }
}
