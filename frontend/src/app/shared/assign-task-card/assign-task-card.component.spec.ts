import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignTaskCardComponent } from './assign-task-card.component';

describe('AssignTaskCardComponent', () => {
  let component: AssignTaskCardComponent;
  let fixture: ComponentFixture<AssignTaskCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssignTaskCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssignTaskCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
