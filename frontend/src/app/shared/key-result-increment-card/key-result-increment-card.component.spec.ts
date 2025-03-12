import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultIncrementCardComponent } from './key-result-increment-card.component';

describe('KeyResultIncrementCardComponent', () => {
  let component: KeyResultIncrementCardComponent;
  let fixture: ComponentFixture<KeyResultIncrementCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KeyResultIncrementCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(KeyResultIncrementCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
