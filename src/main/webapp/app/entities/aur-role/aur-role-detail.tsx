import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './aur-role.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurRoleDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const aurRoleEntity = useAppSelector(state => state.aurRole.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aurRoleDetailsHeading">
          <Translate contentKey="wmsApp.aurRole.detail.title">AurRole</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{aurRoleEntity.id}</dd>
          <dt>
            <span id="roleId">
              <Translate contentKey="wmsApp.aurRole.roleId">Role Id</Translate>
            </span>
          </dt>
          <dd>{aurRoleEntity.roleId}</dd>
          <dt>
            <span id="roleName">
              <Translate contentKey="wmsApp.aurRole.roleName">Role Name</Translate>
            </span>
          </dt>
          <dd>{aurRoleEntity.roleName}</dd>
          <dt>
            <span id="companyCode">
              <Translate contentKey="wmsApp.aurRole.companyCode">Company Code</Translate>
            </span>
          </dt>
          <dd>{aurRoleEntity.companyCode}</dd>
        </dl>
        <Button tag={Link} to="/aur-role" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/aur-role/${aurRoleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AurRoleDetail;
