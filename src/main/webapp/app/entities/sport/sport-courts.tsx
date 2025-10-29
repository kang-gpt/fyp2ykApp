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

  useEffect(() => {
    dispatch(getEntities({}));
  }, [dispatch]);

  useEffect(() => {
    if (sportList && sportId) {
      const selectedSport = sportList.find(sport => sport.id === parseInt(sportId, 10));
      if (selectedSport) {
        setSportName(selectedSport.name);
      }
    }
  }, [sportList, sportId]);

  const generateCourts = (name: string) => {
    const courts = [];
    let count = 0;
    switch (name.toLowerCase()) {
      case 'pickleball':
        count = 8;
        break;
      case 'badminton':
        count = 8;
        break;
      case 'basketball':
        count = 4;
        break;
      case 'futsal':
        count = 4;
        break;
      default:
        return [];
    }

    for (let i = 1; i <= count; i++) {
      courts.push(`${name} Court ${i}`);
    }
    return courts;
  };

  const courts = generateCourts(sportName);

  return (
    <div>
      <h2 id="sport-courts-heading" data-cy="SportCourtsHeading">
        <Translate contentKey="ykApp.sport.courts.title" interpolate={{ sportName }}>
          Courts for {sportName}
        </Translate>
      </h2>
      <div className="table-responsive">
        {courts && courts.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="ykApp.sport.courts.name">Court Name</Translate>
                </th>
              </tr>
            </thead>
            <tbody>
              {courts.map((courtName, i) => (
                <tr key={`court-${i}`}>
                  <td>{courtName}</td>
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
