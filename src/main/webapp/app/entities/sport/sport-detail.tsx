import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sport.reducer';

export const SportDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sportEntity = useAppSelector(state => state.sport.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sportDetailsHeading">
          <Translate contentKey="ykApp.sport.detail.title">Sport</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{sportEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="ykApp.sport.name">Name</Translate>
            </span>
          </dt>
          <dd>{sportEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/sports" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sports/${sportEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SportDetail;
