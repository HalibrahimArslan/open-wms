import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './aur-user.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurUserDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const aurUserEntity = useAppSelector(state => state.aurUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aurUserDetailsHeading">
          <Translate contentKey="wmsApp.aurUser.detail.title">AurUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{aurUserEntity.id}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="wmsApp.aurUser.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{aurUserEntity.userId}</dd>
          <dt>
            <span id="userName">
              <Translate contentKey="wmsApp.aurUser.userName">User Name</Translate>
            </span>
          </dt>
          <dd>{aurUserEntity.userName}</dd>
          <dt>
            <span id="companyCode">
              <Translate contentKey="wmsApp.aurUser.companyCode">Company Code</Translate>
            </span>
          </dt>
          <dd>{aurUserEntity.companyCode}</dd>
        </dl>
        <Button tag={Link} to="/aur-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/aur-user/${aurUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AurUserDetail;
