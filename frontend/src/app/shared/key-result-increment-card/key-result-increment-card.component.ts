import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { KeyResultIncrementService } from '../../core/services/key-result-increment.service';

@Component({
  selector: 'app-key-result-increment-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './key-result-increment-card.component.html',
  styleUrl: './key-result-increment-card.component.scss'
})
export class KeyResultIncrementCardComponent implements OnInit {
  @Input() taskId!: number;
  @Input() show: boolean = false;

  @Output() onSubmit = new EventEmitter<number>();
  @Output() onCancel = new EventEmitter<void>();

  keyResultName: string = '';
  currentValue: number = 0;
  targetValue: number = 0;
  unit: string = '';
  tempCurrentValue: number = 0;
  error: string | null = null;

  constructor(private keyResultService: KeyResultIncrementService) {}

  ngOnInit() {
    if (this.show && this.taskId) {
      this.loadKeyResultProgress();
    }
  }

  ngOnChanges() {
    if (this.show && this.taskId) {
      this.loadKeyResultProgress();
    }
  }

  private loadKeyResultProgress() {
    this.keyResultService.getKeyResultProgress(this.taskId).subscribe({
      next: (data) => {
        console.log(data);
        this.keyResultName = data.keyResultName;
        this.currentValue = data.keyResultCurrentVal;
        this.targetValue = data.keyResultTargetVal;
        this.unit = data.keyResultUnit;
        this.tempCurrentValue = this.currentValue;
        this.error = null;
        console.log("hey",this.targetValue);
      },
      error: (err) => {
        this.error = 'Failed to load key result progress';
        console.error('Error loading key result progress:', err);
      }
    });
  }

  submit() {
    if (this.tempCurrentValue > this.targetValue) {
      this.error = 'New value cannot exceed target value';
      return;
    }
    if (this.tempCurrentValue < this.currentValue) {
      this.error = 'New value cannot be less than current value';
      return;
    }
    this.onSubmit.emit(this.tempCurrentValue);
  }

  cancel() {
    this.tempCurrentValue = this.currentValue;
    this.onCancel.emit();
  }
}
