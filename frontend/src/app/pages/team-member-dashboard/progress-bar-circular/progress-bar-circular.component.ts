import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-progress-bar-circular',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './progress-bar-circular.component.html',
  styleUrl: './progress-bar-circular.component.scss'
})
export class ProgressBarCircularComponent {
  @Input() percentage: number = 0;
}
