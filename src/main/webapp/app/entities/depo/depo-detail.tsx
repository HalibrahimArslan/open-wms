import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './warehouse.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DepoDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const depoEntity = useAppSelector(state => state.depo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="depoDetailsHeading">
          <Translate contentKey="wmsApp.warehouse.detail.title">Depo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{depoEntity.id}</dd>
          <dt>
            <span id="depoNo">
              <Translate contentKey="wmsApp.warehouse.depoNo">Depo No</Translate>
            </span>
          </dt>
          <dd>{depoEntity.code}</dd>
          <dt>
            <span id="depoName">
              <Translate contentKey="wmsApp.warehouse.depoName">Depo Name</Translate>
            </span>
          </dt>
          <dd>{depoEntity.name}</dd>
          <dt>
            <span id="companyCode">
              <Translate contentKey="wmsApp.warehouse.companyCode">Company Code</Translate>
            </span>
          </dt>
          <dd>{depoEntity.companyCode}</dd>
        </dl>
        <Button tag={Link} to="/warehouse" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/warehouse/${depoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DepoDetail;
