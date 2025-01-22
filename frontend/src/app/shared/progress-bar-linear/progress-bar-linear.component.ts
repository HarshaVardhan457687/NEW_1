import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-progress-bar-linear',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './progress-bar-linear.component.html',
  styleUrl: './progress-bar-linear.component.scss'
})
export class ProgressBarLinearComponent {
  @Input() progress: number = 0;
  @Input() color: string = 'status-green';
}
