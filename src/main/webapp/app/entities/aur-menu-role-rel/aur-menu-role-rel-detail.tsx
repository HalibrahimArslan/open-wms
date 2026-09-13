import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './aur-menu-role-rel.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurMenuRoleRelDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const aurMenuRoleRelEntity = useAppSelector(state => state.aurMenuRoleRel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aurMenuRoleRelDetailsHeading">
          <Translate contentKey="wmsApp.aurMenuRoleRel.detail.title">AurMenuRoleRel</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{aurMenuRoleRelEntity.id}</dd>
          <dt>
            <span id="menuId">
              <Translate contentKey="wmsApp.aurMenuRoleRel.menuId">Menu Id</Translate>
            </span>
          </dt>
          <dd>{aurMenuRoleRelEntity.menuId}</dd>
          <dt>
            <span id="roleId">
              <Translate contentKey="wmsApp.aurMenuRoleRel.roleId">Role Id</Translate>
            </span>
          </dt>
          <dd>{aurMenuRoleRelEntity.roleId}</dd>
        </dl>
        <Button tag={Link} to="/aur-menu-role-rel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/aur-menu-role-rel/${aurMenuRoleRelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AurMenuRoleRelDetail;
