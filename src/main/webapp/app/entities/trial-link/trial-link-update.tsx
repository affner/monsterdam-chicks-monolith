import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { createEntity, getEntity, reset, updateEntity } from './trial-link.reducer';

export const TrialLinkUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userLites = useAppSelector(state => state.userLite.entities);
  const trialLinkEntity = useAppSelector(state => state.trialLink.entity);
  const loading = useAppSelector(state => state.trialLink.loading);
  const updating = useAppSelector(state => state.trialLink.updating);
  const updateSuccess = useAppSelector(state => state.trialLink.updateSuccess);

  const handleClose = () => {
    navigate(`/trial-link${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserLites({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.freeDays !== undefined && typeof values.freeDays !== 'number') {
      values.freeDays = Number(values.freeDays);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...trialLinkEntity,
      ...values,
      creator: userLites.find(it => it.id.toString() === values.creator?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          ...trialLinkEntity,
          createdDate: convertDateTimeFromServer(trialLinkEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(trialLinkEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(trialLinkEntity.deletedDate),
          creator: trialLinkEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.trialLink.home.createOrEditLabel" data-cy="TrialLinkCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.trialLink.home.createOrEditLabel">Create or edit a TrialLink</Translate>
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
                  id="trial-link-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.trialLink.linkCode')}
                id="trial-link-linkCode"
                name="linkCode"
                data-cy="linkCode"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.trialLink.startDate')}
                id="trial-link-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.trialLink.endDate')}
                id="trial-link-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.trialLink.freeDays')}
                id="trial-link-freeDays"
                name="freeDays"
                data-cy="freeDays"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.trialLink.isUsed')}
                id="trial-link-isUsed"
                name="isUsed"
                data-cy="isUsed"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.trialLink.createdDate')}
                id="trial-link-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.trialLink.lastModifiedDate')}
                id="trial-link-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.trialLink.createdBy')}
                id="trial-link-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.trialLink.lastModifiedBy')}
                id="trial-link-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.trialLink.deletedDate')}
                id="trial-link-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="trial-link-creator"
                name="creator"
                data-cy="creator"
                label={translate('monsterdamChicksApp.trialLink.creator')}
                type="select"
              >
                <option value="" key="0" />
                {userLites
                  ? userLites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/trial-link" replace color="info">
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

export default TrialLinkUpdate;
