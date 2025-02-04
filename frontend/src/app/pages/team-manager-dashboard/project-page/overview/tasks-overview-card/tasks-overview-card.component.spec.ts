import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TasksOverviewCardComponent } from './tasks-overview-card.component';

describe('TasksOverviewCardComponent', () => {
  let component: TasksOverviewCardComponent;
  let fixture: ComponentFixture<TasksOverviewCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TasksOverviewCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TasksOverviewCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
