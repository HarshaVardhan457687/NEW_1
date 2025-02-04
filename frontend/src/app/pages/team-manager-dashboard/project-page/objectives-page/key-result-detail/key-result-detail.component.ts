import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KeyResult } from '../../../../../core/services/objectives.service';
import { ProgressBarLinearComponent } from '../../../../../shared/progress-bar-linear/progress-bar-linear.component';

@Component({
  selector: 'app-key-result-detail',
  standalone: true,
  imports: [CommonModule, ProgressBarLinearComponent],
  templateUrl: './key-result-detail.component.html',
  styleUrls: ['./key-result-detail.component.scss']
})
export class KeyResultDetailComponent implements OnInit {
  @Input() keyResult!: KeyResult;
  profilePic: string = '';

  ngOnInit() {
    // Randomly select a profile picture
    const picNum = Math.floor(Math.random() * 5) + 1;
    this.profilePic = `assets/pic${picNum}.png`;
  }
}
