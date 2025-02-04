import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectivesSectionComponent } from './objectives-section.component';

describe('ObjectivesSectionComponent', () => {
  let component: ObjectivesSectionComponent;
  let fixture: ComponentFixture<ObjectivesSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ObjectivesSectionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ObjectivesSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
