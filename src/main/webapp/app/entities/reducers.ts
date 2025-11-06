import client from 'app/entities/client/client.reducer';
import court from 'app/entities/court/court.reducer';
import sport from 'app/entities/sport/sport.reducer';
import booking from 'app/entities/booking/booking.reducer';
import timeSlot from 'app/entities/time-slot/time-slot.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  client,
  court,
  sport,
  booking,
  timeSlot,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
