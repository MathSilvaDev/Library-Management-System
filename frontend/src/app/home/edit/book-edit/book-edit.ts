import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-book-edit',
  imports: [],
  templateUrl: './book-edit.html',
  styleUrl: './book-edit.scss',
})
export class BookEdit {

  id!: number;

  constructor(private route: ActivatedRoute){}

  ngOnInit(){
    this.id = Number(this.route.snapshot.paramMap.get('id'));
  }
}
