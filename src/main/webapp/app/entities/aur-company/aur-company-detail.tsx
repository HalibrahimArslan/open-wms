import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './aur-company.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurCompanyDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const aurCompanyEntity = useAppSelector(state => state.aurCompany.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aurCompanyDetailsHeading">
          <Translate contentKey="wmsApp.aurCompany.detail.title">AurCompany</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{aurCompanyEntity.id}</dd>
          <dt>
            <span id="companyCode">
              <Translate contentKey="wmsApp.aurCompany.companyCode">Company Code</Translate>
            </span>
          </dt>
          <dd>{aurCompanyEntity.companyCode}</dd>
          <dt>
            <span id="companyName">
              <Translate contentKey="wmsApp.aurCompany.companyName">Company Name</Translate>
            </span>
          </dt>
          <dd>{aurCompanyEntity.companyName}</dd>
          <dt>
            <span id="erpTipi">
              <Translate contentKey="wmsApp.aurCompany.erpTipi">Erp Tipi</Translate>
            </span>
          </dt>
          <dd>{aurCompanyEntity.erpTipi}</dd>
          <dt>
            <span id="apiEndPoint">
              <Translate contentKey="wmsApp.aurCompany.apiEndPoint">Api EndPoint</Translate>
            </span>
          </dt>
          <dd>{aurCompanyEntity.apiEndPoint}</dd>
          <dt>
            <span id="apiParameters">
              <Translate contentKey="wmsApp.aurCompany.apiParameters">Api Parameters</Translate>
            </span>
          </dt>
          <dd>{aurCompanyEntity.apiParameters}</dd>
        </dl>
        <Button tag={Link} to="/aur-company" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/aur-company/${aurCompanyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AurCompanyDetail;
