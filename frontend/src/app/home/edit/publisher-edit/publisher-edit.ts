import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-publisher-edit',
  imports: [],
  templateUrl: './publisher-edit.html',
  styleUrl: './publisher-edit.scss',
})
export class PublisherEdit {

  id!: number;

  constructor(private route: ActivatedRoute){}

  ngOnInit(){
    this.id = Number(this.route.snapshot.paramMap.get('id'));
  }
}
