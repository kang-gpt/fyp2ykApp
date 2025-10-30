import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './court.reducer';
import { getEntity as getSportEntity } from 'app/entities/sport/sport.reducer';

export const Court = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const queryParams = new URLSearchParams(pageLocation.search);
  const selectedSport = queryParams.get('sport');

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const courtList = useAppSelector(state => state.court.entities);
  const loading = useAppSelector(state => state.court.loading);
  const sport = useAppSelector(state => state.sport.entity);

  useEffect(() => {
    if (selectedSport) {
      dispatch(getSportEntity(selectedSport));
      dispatch(getEntities({ sort: `${sortState.sort},${sortState.order}`, sportName: sport.name }));
    } else {
      dispatch(getEntities({ sort: `${sortState.sort},${sortState.order}` }));
    }
  }, [selectedSport, sortState]);

  const sort = (p: string) => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    if (selectedSport) {
      dispatch(getEntities({ sort: `${sortState.sort},${sortState.order}`, sportName: selectedSport }));
    } else {
      dispatch(getEntities({ sort: `${sortState.sort},${sortState.order}` }));
    }
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="court-heading" data-cy="CourtHeading">
        {selectedSport ? (
          <>
            Courts for <strong>{selectedSport}</strong>
          </>
        ) : (
          <Translate contentKey="ykApp.court.home.title">Courts</Translate>
        )}
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="ykApp.court.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/court/new" className="btn btn-primary jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" /> <Translate contentKey="ykApp.court.home.createLabel">Create new Court</Translate>
          </Link>
        </div>
      </h2>

      <div className="table-responsive">
        {courtList && courtList.length > 0 ? (
          <Table responsive hover>
            <thead>
              <tr>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="ykApp.court.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th>
                  <Translate contentKey="ykApp.court.sport">Sport</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {courtList.map((court, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{court.name}</td>
                  <td>{court.sport ? court.sport.name : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/${sport.id}/court/${court.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              {selectedSport ? (
                <>No courts found for {selectedSport}</>
              ) : (
                <Translate contentKey="ykApp.court.home.notFound">No Courts found</Translate>
              )}
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Court;
