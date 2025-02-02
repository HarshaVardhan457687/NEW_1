import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectiveStatCardComponent } from './objective-stat-card.component';

describe('ObjectiveStatCardComponent', () => {
  let component: ObjectiveStatCardComponent;
  let fixture: ComponentFixture<ObjectiveStatCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ObjectiveStatCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ObjectiveStatCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
