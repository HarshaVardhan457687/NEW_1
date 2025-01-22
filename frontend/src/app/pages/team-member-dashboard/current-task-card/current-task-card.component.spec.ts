import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentTaskCardComponent } from './current-task-card.component';

describe('CurrentTaskCardComponent', () => {
  let component: CurrentTaskCardComponent;
  let fixture: ComponentFixture<CurrentTaskCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CurrentTaskCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CurrentTaskCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
