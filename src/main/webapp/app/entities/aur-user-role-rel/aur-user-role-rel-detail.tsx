import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './aur-user-role-rel.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurUserRoleRelDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const aurUserRoleRelEntity = useAppSelector(state => state.aurUserRoleRel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aurUserRoleRelDetailsHeading">
          <Translate contentKey="wmsApp.aurUserRoleRel.detail.title">AurUserRoleRel</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{aurUserRoleRelEntity.id}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="wmsApp.aurUserRoleRel.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{aurUserRoleRelEntity.userId}</dd>
          <dt>
            <span id="roleId">
              <Translate contentKey="wmsApp.aurUserRoleRel.roleId">Role Id</Translate>
            </span>
          </dt>
          <dd>{aurUserRoleRelEntity.roleId}</dd>
        </dl>
        <Button tag={Link} to="/aur-user-role-rel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/aur-user-role-rel/${aurUserRoleRelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AurUserRoleRelDetail;
