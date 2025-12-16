import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ModerationActionType } from 'app/shared/model/enumerations/moderation-action-type.model';
import { createEntity, getEntity, reset, updateEntity } from './moderation-action.reducer';

export const ModerationActionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const moderationActionEntity = useAppSelector(state => state.moderationAction.entity);
  const loading = useAppSelector(state => state.moderationAction.loading);
  const updating = useAppSelector(state => state.moderationAction.updating);
  const updateSuccess = useAppSelector(state => state.moderationAction.updateSuccess);
  const moderationActionTypeValues = Object.keys(ModerationActionType);

  const handleClose = () => {
    navigate(`/moderation-action${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    values.actionDate = convertDateTimeToServer(values.actionDate);
    if (values.ticketId !== undefined && typeof values.ticketId !== 'number') {
      values.ticketId = Number(values.ticketId);
    }

    const entity = {
      ...moderationActionEntity,
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
      ? {
          actionDate: displayDefaultDateTime(),
        }
      : {
          actionType: 'WARNING',
          ...moderationActionEntity,
          actionDate: convertDateTimeFromServer(moderationActionEntity.actionDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.moderationAction.home.createOrEditLabel" data-cy="ModerationActionCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.moderationAction.home.createOrEditLabel">
              Create or edit a ModerationAction
            </Translate>
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
                  id="moderation-action-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.moderationAction.actionType')}
                id="moderation-action-actionType"
                name="actionType"
                data-cy="actionType"
                type="select"
              >
                {moderationActionTypeValues.map(moderationActionType => (
                  <option value={moderationActionType} key={moderationActionType}>
                    {translate(`monsterdamChicksApp.ModerationActionType.${moderationActionType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.moderationAction.reason')}
                id="moderation-action-reason"
                name="reason"
                data-cy="reason"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moderationAction.actionDate')}
                id="moderation-action-actionDate"
                name="actionDate"
                data-cy="actionDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moderationAction.durationDays')}
                id="moderation-action-durationDays"
                name="durationDays"
                data-cy="durationDays"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moderationAction.ticketId')}
                id="moderation-action-ticketId"
                name="ticketId"
                data-cy="ticketId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/moderation-action" replace color="info">
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

export default ModerationActionUpdate;
