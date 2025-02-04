import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectivesDetailCardComponent } from './objectives-detail-card.component';

describe('ObjectivesDetailCardComponent', () => {
  let component: ObjectivesDetailCardComponent;
  let fixture: ComponentFixture<ObjectivesDetailCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ObjectivesDetailCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ObjectivesDetailCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
