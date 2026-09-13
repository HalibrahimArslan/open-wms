import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './aur-user-role-rel.reducer';
import { IAurUserRoleRel } from 'app/shared/model/aur-user-role-rel.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurUserRoleRelUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const aurUserRoleRelEntity = useAppSelector(state => state.aurUserRoleRel.entity);
  const loading = useAppSelector(state => state.aurUserRoleRel.loading);
  const updating = useAppSelector(state => state.aurUserRoleRel.updating);
  const updateSuccess = useAppSelector(state => state.aurUserRoleRel.updateSuccess);

  const handleClose = () => {
    props.history.push('/aur-user-role-rel');
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
      ...aurUserRoleRelEntity,
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
          ...aurUserRoleRelEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="wmsApp.aurUserRoleRel.home.createOrEditLabel" data-cy="AurUserRoleRelCreateUpdateHeading">
            <Translate contentKey="wmsApp.aurUserRoleRel.home.createOrEditLabel">Create or edit a AurUserRoleRel</Translate>
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
                  id="aur-user-role-rel-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('wmsApp.aurUserRoleRel.userId')}
                id="aur-user-role-rel-userId"
                name="userId"
                data-cy="userId"
                type="text"
              />
              <ValidatedField
                label={translate('wmsApp.aurUserRoleRel.roleId')}
                id="aur-user-role-rel-roleId"
                name="roleId"
                data-cy="roleId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/aur-user-role-rel" replace color="info">
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

export default AurUserRoleRelUpdate;
