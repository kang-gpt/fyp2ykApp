import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Button, Table } from 'reactstrap';
import { useAppSelector } from 'app/config/store';
import { getEntities } from './sport.reducer';
import { useAppDispatch } from 'app/config/store';

export const SportCourts = () => {
  const { sportId } = useParams<{ sportId: string }>();

  const dispatch = useAppDispatch();

  const sportList = useAppSelector(state => state.sport.entities);

  const [sportName, setSportName] = useState('');

  const [courts, setCourts] = useState([]);

  useEffect(() => {
    dispatch(getEntities({}));

    fetch('content/data/courts.json')
      .then(response => response.json())

      .then(data => setCourts(data));
  }, [dispatch]);

  useEffect(() => {
    if (sportList && sportId) {
      const selectedSport = sportList.find(sport => sport.id === parseInt(sportId, 10));

      if (selectedSport) {
        setSportName(selectedSport.name);
      }
    }
  }, [sportList, sportId]);

  const filteredCourts = courts.filter(court => court.sport.id === parseInt(sportId, 10));

  return (
    <div>
      <h2 id="sport-courts-heading" data-cy="SportCourtsHeading">
        <Translate contentKey="ykApp.sport.courts.title" interpolate={{ sportName }}>
          Courts for {sportName}
        </Translate>
      </h2>

      <div className="table-responsive">
        {filteredCourts && filteredCourts.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="ykApp.sport.courts.name">Court Name</Translate>
                </th>
              </tr>
            </thead>

            <tbody>
              {filteredCourts.map((court, i) => (
                <tr key={`court-${i}`}>
                  <td>{court.name}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">
            <Translate contentKey="ykApp.sport.courts.notFound">No courts found for this sport.</Translate>
          </div>
        )}
      </div>

      <Button tag={Link} to="/sport" replace color="info" data-cy="entityDetailsBackButton">
        <Translate contentKey="entity.action.back">Back</Translate>
      </Button>
    </div>
  );
};

export default SportCourts;
