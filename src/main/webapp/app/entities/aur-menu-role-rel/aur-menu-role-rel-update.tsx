import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './aur-menu-role-rel.reducer';
import { IAurMenuRoleRel } from 'app/shared/model/aur-menu-role-rel.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurMenuRoleRelUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const aurMenuRoleRelEntity = useAppSelector(state => state.aurMenuRoleRel.entity);
  const loading = useAppSelector(state => state.aurMenuRoleRel.loading);
  const updating = useAppSelector(state => state.aurMenuRoleRel.updating);
  const updateSuccess = useAppSelector(state => state.aurMenuRoleRel.updateSuccess);

  const handleClose = () => {
    props.history.push('/aur-menu-role-rel');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...aurMenuRoleRelEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...aurMenuRoleRelEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="wmsApp.aurMenuRoleRel.home.createOrEditLabel" data-cy="AurMenuRoleRelCreateUpdateHeading">
            <Translate contentKey="wmsApp.aurMenuRoleRel.home.createOrEditLabel">Create or edit a AurMenuRoleRel</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="aur-menu-role-rel-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('wmsApp.aurMenuRoleRel.menuId')}
                id="aur-menu-role-rel-menuId"
                name="menuId"
                data-cy="menuId"
                type="text"
              />
              <ValidatedField
                label={translate('wmsApp.aurMenuRoleRel.roleId')}
                id="aur-menu-role-rel-roleId"
                name="roleId"
                data-cy="roleId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/aur-menu-role-rel" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AurMenuRoleRelUpdate;
