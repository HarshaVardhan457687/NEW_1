import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentTasksSectionComponent } from './current-tasks-section.component';

describe('CurrentTasksSectionComponent', () => {
  let component: CurrentTasksSectionComponent;
  let fixture: ComponentFixture<CurrentTasksSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CurrentTasksSectionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CurrentTasksSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
